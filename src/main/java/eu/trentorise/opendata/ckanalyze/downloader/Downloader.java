package eu.trentorise.opendata.ckanalyze.downloader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * This class provides an easy way to download files in single thread with the
 * resume functionality. It could be used as a standard class (creating an
 * instance) or using the singleton pattern (using the getInstance method).
 * 
 * @author Alberto Zanella <a.zanella@trentorise.eu>
 * 
 */

public class Downloader {
	private String url;
	private String filepath;
	private String filename;
	private long size;
	private static Downloader instance = null;

	
	
	public String getFilename() {
		return filename;
	}

	
	public long getSize() {
		return size;
	}

	

	/**
	 * 
	 * @param url
	 *            - The URL to retrieve
	 * @param filepath
	 *            - The path of the file (requires "/" char at the end)
	 */
	public Downloader(String url, String filepath) {
		this.filepath = filepath;
		this.url = url;
		if(instance == null) instance = this;
	}
	
	private Downloader()
	{
		super();
	}

	/**
	 * You need to specify Url and filepath using setters 
	 */	
	public static Downloader getInstance() {
		if (instance == null) {
			instance = new Downloader();
		}
		return instance;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}


	/**
	 * Download using properties values
	 */
	public void download() {
		download(url, filepath);
	}
	
	/**
	 * 
	 * @param url
	 *            - The URL to retrieve
	 * @param filepath
	 *            - The path of the file (requires "/" char at the end)
	 */

	public void download(String url, String filepath) {
		BufferedInputStream in = null;
		java.io.FileOutputStream fos = null;
		long remoteSize = -1;
		try {
			URL urlo = new URL(url);
			URLConnection connection = null;
			connection = urlo.openConnection();
			// Get real filename in dynamic urls
			String rawFn = connection.getHeaderField("Content-Disposition");
			if (rawFn != null && rawFn.contains("attachment")) {
				rawFn = rawFn.replace("attachment; filename=\"", "");
				rawFn = rawFn.replaceAll("\"", "");
				filename = rawFn;
			} else {
						filename = urlo.getPath().substring(
								urlo.getPath().lastIndexOf("/") + 1);
			}
			filepath = filepath + filename;
			boolean skip = false;
			File fcheck = new File(filepath);

			if ((connection.getHeaderFields().get("Content-Length") != null)
					&& (!connection.getHeaderFields().get("Content-Length")
							.isEmpty())) {
				remoteSize = Long.parseLong(connection.getHeaderFields()
						.get("Content-Length").get(0));
			}

			long localSize = fcheck.length();
			if (fcheck.exists() && remoteSize > 0)
				skip = remoteSize == localSize;
			connection = urlo.openConnection();
			if (fcheck.exists()) {
				connection.setRequestProperty("Range",
						"Bytes=" + (fcheck.length()) + "-");
			}
			
			boolean redownload = false;
			
			if (!skip) {
				connection.setDoInput(true);
				connection.setDoOutput(true);
				try
				{
				in = new BufferedInputStream(connection.getInputStream());
				}catch(IOException e)
				{
					connection = urlo.openConnection();
					in = new BufferedInputStream(connection.getInputStream());
					redownload = true;
				}

				if (fcheck.exists()) {
					if ((!redownload)||(connection.getHeaderField("Accept-Ranges") != null
							&& connection.getHeaderField("Accept-Ranges")
									.equals("bytes"))) {
						fos = new java.io.FileOutputStream(filepath, true);
					} else {
						fos = new java.io.FileOutputStream(filepath);
					}
				} else {
					fcheck.createNewFile();
					fos = new java.io.FileOutputStream(filepath);
				}

				byte data[] = new byte[1024];
				int count;
				while ((count = in.read(data, 0, 1024)) != -1) {
					fos.write(data, 0, count);
				}
			}
			instance.size = new File(filepath).length();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (fos != null)
					fos.close();
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
