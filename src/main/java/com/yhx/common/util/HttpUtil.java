package com.yhx.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Slf4j
public class HttpUtil {

	/**
	 * 向指定URL发送GET方法的请求
	 *
	 * @param url
	 *            发送请求的URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return URL 所代表远程资源的响应结果
	 */
	public static String sendGet(String url, String param) {
		String result = "";
		BufferedReader in = null;
		try {
			String urlNameString = url
					+ (StringUtils.isNotEmpty(param) ? "?" + param : "");
			URL realUrl = new URL(urlNameString);
			// 打开和URL之间的连接
			URLConnection connection = realUrl.openConnection();
			// 设置通用的请求属性
			connection.setRequestProperty("accept", "*/*");
			connection.setRequestProperty("connection", "Keep-Alive");
			connection.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			// 建立实际的连接
			connection.connect();
			// 获取所有响应头字段
			// 定义 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(connection.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			log.error("发送GET请求出现异常！" ,e);
		}
		// 使用finally块来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				log.error("关闭输入流异常！" ,e2);
			}
		}
		return result;
	}

	public static void main(String[] args) {
		String url = "http://localhost:8080/bms/sysParameter/listData.do";
		String result = sendGet(url);
		System.out.println(result);
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		return sendPost(url, param, null);
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 *
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param,
			String contentType) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent",
					"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			if (StringUtils.isNotEmpty(contentType))
				conn.setRequestProperty("Content-Type", contentType);
			// conn.setRequestProperty("auth",
			// "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiIxIiwiand0VG9rZW4iOiI1MEQyNkU5RS1DMEE1LTQ0Q0EtQjAxRi01MkJGODlFQ0M4MDgiLCJleHAiOjE0NzE0MTU0MTUsImlhdCI6MTQ3MTQxMzYxNX0.LbpeTL1YI98B19SnxjdoxWerFY2o6pcJ8Q-ULLaenM0");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			log.error("发送 POST 请求出现异常！" ,e);
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				log.error("关闭输出流、输入流：" ,ex);
			}
		}
		return result;
	}

	public static String sendGet(String url) {
		return sendGet(url, "");
	}

	public static String sendGet(String url, Map<String, String> param) {
		return sendGet(url, paramString(param));
	}

	public static String sendPost(String url, Map<String, String> param) {
		return sendPost(url, paramString(param));
	}
	
	public static String sendPost(String url, Map<String, String> param, String contentType) {
		return sendPost(url, paramString(param), contentType);
	}

	private static String paramString(Map<String, String> param) {
		if (param == null || param.isEmpty()) {
			return null;
		}

		Set<String> keys = param.keySet();

		Iterator<String> it = keys.iterator();
		StringBuffer sb = new StringBuffer();
		while (it.hasNext()) {
			String key = it.next();
			sb.append(key).append("=").append(param.get(key))
					.append("&");
		}
		return sb.substring(0, sb.length() - 1);
	}

	/**
	 * 上传图片
	 * 
	 * @param url
	 * @param files
	 * @return
	 */
	public static String upload(String url, File[] files) {
		try {
			String boundary = "------WebKitFormBoundaryUey8ljRiiZqhZHBu";
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("Charsert", "UTF-8");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			// 指定流的大小，当内容达到这个值的时候就把流输出
			conn.setChunkedStreamingMode(10240);
			OutputStream out = new DataOutputStream(conn.getOutputStream());
			byte[] end_data = ("\r\n--" + boundary + "--\r\n").getBytes();// 定义最后数据分隔线
			StringBuilder sb = new StringBuilder();
			for (File file : files) {
				sb = new StringBuilder();
				sb.append("--");
				sb.append(boundary);
				sb.append("\r\n");
				sb.append("Content-Disposition: form-data;name=\"files"
						+ "\";filename=\"" + file.getName() + "\"\r\n");
				sb.append("Content-Type:application/octet-stream\r\n\r\n");
				byte[] data = sb.toString().getBytes();
				out.write(data);
				DataInputStream in = new DataInputStream(
						new FileInputStream(file));
				int bytes = 0;
				byte[] bufferOut = new byte[1024];
				while ((bytes = in.read(bufferOut)) != -1) {
					out.write(bufferOut, 0, bytes);
				}
				out.write("\r\n".getBytes()); // 多个文件时，二个文件之间加入这个
				in.close();
			}
			out.write(end_data);
			out.flush();
			out.close();
			// 定义BufferedReader输入流来读取URL的响应
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String result = "";
			String line = "";
			while ((line = reader.readLine()) != null && line.length() > 0) {
				result += line;
			}

			return result;
		} catch (Exception e) {
			log.error("发送POST请求出现异常！",e);
		}
		return null;
	}
}
