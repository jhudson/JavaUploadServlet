package org.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

public class UploadServlet extends HttpServlet {
  private static final long serialVersionUID = 6748857432950840322L;
  private static final String DESTINATION_DIR_PATH = "files";
  private static String realPath;

  /**
   * {@inheritDoc}
   * 
   * @param config
   * @throws ServletException
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    realPath = getServletContext().getRealPath(DESTINATION_DIR_PATH) + "/";
  }

  /**
   * Handles the HTTP <code>POST</code> method.
   * 
   * @param request
   *          servlet request
   * @param response
   *          servlet response
   * @throws ServletException
   *           if a servlet-specific error occurs
   * @throws IOException
   *           if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException {

    PrintWriter writer = null;
    InputStream is = null;
    FileOutputStream fos = null;

    try {
      writer = response.getWriter();
    } catch (IOException ex) {
      log(UploadServlet.class.getName() + "has thrown an exception: " + ex.getMessage());
    }

    String filename = request.getHeader("X-File-Name");
    try {
      is = request.getInputStream();
      fos = new FileOutputStream(new File(realPath + filename));
      IOUtils.copy(is, fos);
      response.setStatus(response.SC_OK);
      writer.print("{success: true}");
    } catch (FileNotFoundException ex) {
      response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
      writer.print("{success: false}");
      log(UploadServlet.class.getName() + "has thrown an exception: " + ex.getMessage());
    } catch (IOException ex) {
      response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
      writer.print("{success: false}");
      log(UploadServlet.class.getName() + "has thrown an exception: " + ex.getMessage());
    } finally {
      try {
        fos.close();
        is.close();
      } catch (IOException ignored) {
      }
    }

    writer.flush();
    writer.close();
  }
}