package com.example;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.Product;


/**
 * Servlet implementation class PetsServlet
 */
public class PetsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PetsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// TODO Auto-generated method stub
        try {
               SessionFactory factory = HibernateUtil.getSessionFactory();
               
               Session session = factory.openSession();
               // using HQL
               List<Product> list = session.createQuery("from Product", Product.class).list();
               
               session.close();
               
                PrintWriter out = response.getWriter();
                out.println("<html><body>");
                out.println("<b>Product Listing</b><br>");
                for(Product p: list) {
                        out.println("ID: " + String.valueOf(p.getID()) + ", Name: " + p.getName() +
                                        ", Price: " + String.valueOf(p.getPrice()) + ", Color: " + p.getColor().toString() + "<br>");
                }
                
            out.println("</body></html>");
            
            
        } catch (Exception ex) {
                throw ex;
        }

		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

        PrintWriter out = response.getWriter();
        String name = request.getParameter("name");
        String color = request.getParameter("color");
        String price = request.getParameter("price");
        if((name == null || name.isBlank()) || (color == null || color.isBlank()) || (price == null || price.isBlank()))
		{
			response.sendRedirect("blank.jsp");
			return;
		}
        
        Pattern m = Pattern.compile("\\d+(\\.\\d+)?");
        if (!m.matcher(price).matches())
        {
        	response.sendRedirect("invalid.jsp");
        	return;
        }
        
        out.println("<html><body>");
        out.println("<b>Adding Pet</b> " + request.getParameter("name") + "<br>");
        out.println("</body></html>");
       
        
        
        try 
        {
        	SessionFactory factory = HibernateUtil.getSessionFactory(); 
        	Session session = factory.openSession();
        	
        	session.beginTransaction();
        	
        	Product p = new Product();
        	p.setName(name);
        	p.setColor(color);
        	p.setPrice(BigDecimal.valueOf(Double.parseDouble(price)));
        	session.save(p);
			session.getTransaction().commit();
			session.close();
			
			 out.println("<html><body>"); 
			 out.println("<b>Success!</b><br>");
			 out.println("<a href='index.jsp'>Return to Main</a><br>");
			 out.println("</body></html>");
        }
        
        catch(Exception ex)
        {
        	throw ex;
        }
        
        
        //TODO: Take all parameters from post, and use hibernate to insert new pet.
        // Then you need to print out some confirmation as to the the success/failure.
        // You also need to validate your input and not allow missing / NULL data.

		//doGet(request, response);
	}
}