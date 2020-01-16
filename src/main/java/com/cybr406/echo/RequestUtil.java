package com.cybr406.echo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Collections;
import java.util.stream.Collectors;

public class RequestUtil {

    public static String requestToString(HttpServletRequest request) throws IOException {
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

    public static void printSectionName(PrintWriter out, String name) {
        out.println("------------------------------------------");
        out.println(name);
        out.println("------------------------------------------");
    }

    public static void printMethod(PrintWriter out, HttpServletRequest request) {
        printSectionName(out, "Request Method");
        out.println(request.getMethod());
    }

    public static void printPath(PrintWriter out, HttpServletRequest request) {
        printSectionName(out, "Request URI");
        out.println(request.getRequestURI());
    }

    public static void printParameters(PrintWriter out, HttpServletRequest request) {
        printSectionName(out, "Parameters");
        if (request.getParameterMap().isEmpty())
            out.println("None");
        else
            request.getParameterMap()
                    .forEach((key, values) -> out.printf("%s: %s\r\n", key, String.join(", ", values)));
    }

    public static void printHeaders(PrintWriter out, HttpServletRequest request) {
        printSectionName(out, "Headers");
        if (!request.getHeaderNames().hasMoreElements())
            out.println("None");
        else
            Collections.list(request.getHeaderNames())
                    .forEach(name -> out.printf("%s: %s\r\n", name, request.getHeader(name)));
    }

    public static void printParts(PrintWriter out, HttpServletRequest request) throws IOException {
        printSectionName(out, "Parts");
        try {
            out.println(request.getParts().stream()
                    .map(RequestUtil::partToString)
                    .collect(Collectors.joining("\r\n\r\n")));
        } catch (ServletException e) {
            out.println("None");
        }
    }

    public static String partToString(Part part) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println(part.getName());
        pw.printf("Submitted file name: %s\r\n", part.getSubmittedFileName());
        pw.printf("Content type: %s\r\n", part.getContentType());
        pw.printf("Size: %d bytes\r\n", part.getSize());

        try {
            InputStream in = part.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int read = 0;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            pw.println("Part Contents:");
            pw.print(out.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sw.toString();
    }

}
