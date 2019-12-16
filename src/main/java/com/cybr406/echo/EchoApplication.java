package com.cybr406.echo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.stream.Collectors;

@RestController
@SpringBootApplication
public class EchoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EchoApplication.class, args);
	}

	@RequestMapping("/echo/**")
	public String echo(HttpServletRequest request) throws IOException, ServletException {
		StringWriter stringWriter = new StringWriter();
		PrintWriter out = new PrintWriter(stringWriter);

		printMethod(out, request);
		out.println();

		printPath(out, request);
		out.println();

		printHeaders(out, request);
		out.println();

		printParameters(out, request);
		out.println();

		printParts(out, request);

		return stringWriter.toString();
	}

	private void printSectionName(PrintWriter out, String name) {
		out.println("------------------------------------------");
		out.println(name);
		out.println("------------------------------------------");
	}

	public void printMethod(PrintWriter out, HttpServletRequest request) {
		printSectionName(out, "Request Method");
		out.println(request.getMethod());
	}

	public void printPath(PrintWriter out, HttpServletRequest request) {
		printSectionName(out, "Request URI");
		out.println(request.getRequestURI());
	}

	public void printParameters(PrintWriter out, HttpServletRequest request) {
		printSectionName(out, "Parameters");
		if (request.getParameterMap().isEmpty())
			out.println("None");
		else
			request.getParameterMap()
					.forEach((key, values) -> out.printf("%s: %s\n", key, String.join(", ", values)));
	}

	public void printHeaders(PrintWriter out, HttpServletRequest request) {
		printSectionName(out, "Headers");
		if (!request.getHeaderNames().hasMoreElements())
			out.println("None");
		else
			Collections.list(request.getHeaderNames())
					.forEach(name -> out.printf("%s: %s\n", name, request.getHeader(name)));
	}

	public void printParts(PrintWriter out, HttpServletRequest request) throws IOException, ServletException {
		printSectionName(out, "Parts");
		try {
			out.println(request.getParts().stream()
					.map(this::partToString)
					.collect(Collectors.joining("\n\n")));
		} catch (ServletException e) {
			out.println("None");
		}
	}

	public String partToString(Part part) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		pw.println(part.getName());
		pw.printf("Submitted file name: %s\n", part.getSubmittedFileName());
		pw.printf("Content type: %s\n", part.getContentType());
		pw.printf("Size: %d bytes", part.getSize());
		return sw.toString();
	}

}
