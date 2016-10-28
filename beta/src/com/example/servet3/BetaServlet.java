package com.example.servet3;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.inject.CounterInterface;

/**
 * Servlet implementation class BetaServlet
 */
@WebServlet("/BetaServlet")
public class BetaServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Inject
	private CounterInterface counter;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public BetaServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		String headbody = "<html><head><title>HCP Injection Test - BetaServlet</title></head><body>";
		String count = "Count: <code>" + counter.getNext() + "</code>";

		String responseBuf = headbody + count + "<p>Served at: " + request.getContextPath() + "</p>" + "</body>";
		response.getWriter().append(responseBuf);
	}

}
