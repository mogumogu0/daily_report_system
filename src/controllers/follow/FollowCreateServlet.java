package controllers.follow;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Employee;
import models.Follow;
import utils.DBUtil;

/**
 * Servlet implementation class FollowCreateServlet
 */
@WebServlet("/follow/create")
public class FollowCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String _token = request.getParameter("_token");
        if(_token != null && _token.equals(request.getSession().getId())) {
            EntityManager em = DBUtil.createEntityManager();

            Follow r = new Follow();

            r.setFollow_id((Employee)request.getSession().getAttribute("login_employee"));

            r.setFollower_id(Integer.parseInt(request.getParameter("_employee")));

            em.getTransaction().begin();
            em.persist(r);
            em.getTransaction().commit();
            em.close();
            request.getSession().setAttribute("flush", "登録が完了しました。");

            response.sendRedirect(request.getContextPath() + "/reports/index");

        }
    }
}
