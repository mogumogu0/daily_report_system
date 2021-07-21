package controllers.follow;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Follow;
import utils.DBUtil;

/**
 * Servlet implementation class FollowDestroyServlet
 */
@WebServlet("/follow/destroy")
public class FollowDestroyServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public FollowDestroyServlet() {
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

                // セッションスコープからメッセージのIDを取得して
                // 該当のIDのメッセージ1件のみをデータベースから取得
                Follow f = em.find(Follow.class, Integer.parseInt(request.getParameter("_follow")));
                em.getTransaction().begin();
                em.remove(f);       // データ削除
                em.getTransaction().commit();
                em.close();

                // セッションスコープ上の不要になったデータを削除
                request.getSession().removeAttribute("_follow");
                response.sendRedirect(request.getContextPath() + "/follow/index");
            }
    }

}
