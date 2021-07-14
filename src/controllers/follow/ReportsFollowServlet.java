package controllers.follow;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import models.Report;
import utils.DBUtil;

/**
 * Servlet implementation class ReportsIndexServlet
 */
@WebServlet("/follow/index")
public class ReportsFollowServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportsFollowServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

        int page;
        try{
            page = Integer.parseInt(request.getParameter("page"));
        } catch(Exception e) {
            page = 1;
        }

        Employee login_employee = (Employee)request.getSession().getAttribute("login_employee");

        List<Follow> follows= em.createNamedQuery("getFollower",Follow.class)
                                .setParameter("employee", login_employee)
                                .getResultList();
        System.out.println("aiuo"+login_employee.getId());
        String where = "";
        for (Follow follow : follows) {
            System.out.println(follow.getFollower_id());
            where += ",";
            where += follow.getFollower_id();
        }
        if (where!=""){
            where=where.substring(1);
        }else{
            where="0";
        }
        System.out.println("-------------------------------------------------------------");
        System.out.println(where);

        List<Report> reports = em.createNamedQuery("getFollowReports", Report.class)
                .setParameter("where", where)
                .setFirstResult(15 * (page - 1))
                .setMaxResults(15)
                .getResultList();

        long reports_count = (long)em.createNamedQuery("getFollowReportsCount", Long.class)
                   .setParameter("where", where)
                   .getSingleResult();
        em.close();


        request.setAttribute("reports", reports);
        request.setAttribute("reports_count", reports_count);
        request.setAttribute("page", page);
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follow/index.jsp");
        rd.forward(request, response);
    }

}