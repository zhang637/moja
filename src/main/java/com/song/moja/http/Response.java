package com.song.moja.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletResponse;

import com.song.moja.util.Constance;

public class Response implements ServletResponse {

  private static final int BUFFER_SIZE = 1024;
  private String uri;
  OutputStream output;
  PrintWriter writer;

  public Response(OutputStream output) {
    this.output = output;
  }

//  public void setRequest(Request request) {
//    this.request = request;
//  }
  	public void setUri(String uri){
  		this.uri = uri;
  	}
  

  /* This method is used to serve a static page */
  public void sendStaticResource() throws IOException {
    byte[] bytes = new byte[BUFFER_SIZE];
    FileInputStream fis = null;
    try {
      /* request.getUri has been replaced by request.getRequestURI */
      File file = new File(Constance.WEB_ROOT, uri);
      fis = new FileInputStream(file);
      /*
         HTTP Response = Status-Line
           *(( general-header | response-header | entity-header ) CRLF)
           CRLF
           [ message-body ]
         Status-Line = HTTP-Version SP Status-Code SP Reason-Phrase CRLF
      */
      int ch = fis.read(bytes, 0, BUFFER_SIZE);
      while (ch!=-1) {
        output.write(bytes, 0, ch);
        ch = fis.read(bytes, 0, BUFFER_SIZE);
      }
    }
    catch (FileNotFoundException e) {
      String errorMessage = "HTTP/1.1 404 File Not Found\r\n" +
        "Content-Type: text/html\r\n" +
        "Content-Length: 23\r\n" +
        "\r\n" +
        "<h1>File Not Found</h1>";
      output.write(errorMessage.getBytes());
    }
    finally {
      if (fis!=null)
        fis.close();
    }
  }

  public PrintWriter getWriter() throws IOException {
    // autoflush is true, println() will flush,
    // but print() will not.
    writer = new PrintWriter(output, true);
    return writer;
  }

public String getCharacterEncoding() {
	// TODO Auto-generated method stub
	return null;
}

public String getContentType() {
	// TODO Auto-generated method stub
	return null;
}

public ServletOutputStream getOutputStream() throws IOException {
	// TODO Auto-generated method stub
	return null;
}

public void setCharacterEncoding(String charset) {
	// TODO Auto-generated method stub
	
}

public void setContentLength(int len) {
	// TODO Auto-generated method stub
	
}

public void setContentLengthLong(long len) {
	// TODO Auto-generated method stub
	
}

public void setContentType(String type) {
	// TODO Auto-generated method stub
	
}

public void setBufferSize(int size) {
	// TODO Auto-generated method stub
	
}

public int getBufferSize() {
	// TODO Auto-generated method stub
	return 0;
}

public void flushBuffer() throws IOException {
	// TODO Auto-generated method stub
	
}

public void resetBuffer() {
	// TODO Auto-generated method stub
	
}

public boolean isCommitted() {
	// TODO Auto-generated method stub
	return false;
}

public void reset() {
	// TODO Auto-generated method stub
	
}

public void setLocale(Locale loc) {
	// TODO Auto-generated method stub
	
}

public Locale getLocale() {
	// TODO Auto-generated method stub
	return null;
}

  

}