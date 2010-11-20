package com.thoughtworks.blipit;

import com.thoughtworks.contract.BlipItRequest;
import com.thoughtworks.contract.BlipItResponse;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class BlipItMainServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/plain");
        resp.getWriter().println("Hi there ! I can only process HTTP post requests !!");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectInputStream objectInputStream = null;
        ServletOutputStream servletOutputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            objectInputStream = new ObjectInputStream(req.getInputStream());
            BlipItRequest blipItRequest = (BlipItRequest) objectInputStream.readObject();
            String message = blipItRequest.getMessage();
            // TODO: log this message
            servletOutputStream = resp.getOutputStream();
            byteArrayOutputStream = new ByteArrayOutputStream();
            prepareResponse(message, byteArrayOutputStream);
            resp.setContentType("application/octet-stream");
            resp.setContentLength(byteArrayOutputStream.size());
            servletOutputStream.write(byteArrayOutputStream.toByteArray());
        } catch (ClassNotFoundException e) {
            // TODO: use appengine logging support to log errors
        } finally {
            if (objectInputStream != null) {
                objectInputStream.close();
            }
            if (servletOutputStream != null) {
                servletOutputStream.flush();
                servletOutputStream.close();
            }
            if (byteArrayOutputStream != null) {
                byteArrayOutputStream.flush();
                byteArrayOutputStream.close();
            }
        }
    }

    private void prepareResponse(String message, ByteArrayOutputStream byteArrayOutputStream) throws IOException {
        ObjectOutputStream objectOutputStream = null;
        try {
            objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            BlipItResponse blipItResponse = new BlipItResponse();
            blipItResponse.setMessage(message);
            objectOutputStream.writeObject(blipItResponse);
        } finally {
            if (objectOutputStream != null) {
                objectOutputStream.flush();
                objectOutputStream.close();
            }
        }
    }
}
