package com.abhi.report.SFTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.abhi.report.exception.ChangeBookingException;
import com.abhi.report.model.GrepStringForm;
import com.abhi.report.model.ProcessDataForm;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class DownloadFileSFTP {

	public List<String> retrieveLogFile(ProcessDataForm processDataForm, String command) throws ChangeBookingException {
		System.out.println("Running command for " + command);
		List<String> list = new ArrayList<>();
		Session session = null;
		Channel channel = null;
		try {
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			config.put("compression.s2c", "zlib,none");
			config.put("compression.c2s", "zlib,none");
			JSch jsch = new JSch();
			session = jsch.getSession(processDataForm.getUserId(), processDataForm.getEnv(), 22);
			session.setPassword(processDataForm.getPassword());
			session.setConfig(config);
			session.connect();
			System.out.println("Connected");
			channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);
			InputStream in = channel.getInputStream();
			channel.connect();

			InputStreamReader stringReader = new InputStreamReader(in);
			BufferedReader bufferedReader = new BufferedReader(stringReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				list.add(line);
			}
		} catch (JSchException | IOException e) {
			throw new ChangeBookingException(e.getMessage());
		} finally {
			if (null != channel) {
				channel.disconnect();
			}
			if (null != session) {
				session.disconnect();
			}
		}

		System.out.println("DONE");
		System.out.println("end command for " + command);
		return list;
	}
	public static List<String> retrieveGrepStringLogFile(GrepStringForm grepStringForm, String command) throws ChangeBookingException {
		System.out.println("Running command for " + command);
		List<String> list = new ArrayList<>();
		Session session = null;
		Channel channel = null;
		try {
			java.util.Properties config = new java.util.Properties();
			config.put("StrictHostKeyChecking", "no");
			config.put("compression.s2c", "zlib,none");
			config.put("compression.c2s", "zlib,none");
			JSch jsch = new JSch();
			session = jsch.getSession(grepStringForm.getUserId(), grepStringForm.getEnv(), 22);
			session.setPassword(grepStringForm.getPassword());
			session.setConfig(config);
			session.connect();
			System.out.println("Connected");
			channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.setInputStream(null);
			InputStream in = channel.getInputStream();
			channel.connect();

			InputStreamReader stringReader = new InputStreamReader(in);
			BufferedReader bufferedReader = new BufferedReader(stringReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				list.add(line);
			}
		} catch (JSchException | IOException e) {
			throw new ChangeBookingException(e.getMessage());
		} finally {
			if (null != channel) {
				channel.disconnect();
			}
			if (null != session) {
				session.disconnect();
			}
		}

		System.out.println("DONE");
		System.out.println("end command for " + command);
		return list;
	}

	
}
