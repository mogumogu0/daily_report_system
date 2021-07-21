package controllers.follow;

import java.io.IOException;
import java.util.ArrayList;
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

        // 結果のリスト
        List<Report> answer = new ArrayList<>();
        List<Report> reports=new ArrayList<>();
        for (Follow follow : follows) {
            reports = em.createNamedQuery("getFollowReports", Report.class)
                    .setParameter("Ffollow", follow.getFollower_id())
                    .getResultList();
          answer.addAll(reports);
        }
        //2ページ目以降のページネーション
        List<Report> PageAnswer=new ArrayList<>();

        if(answer.size()>15) {
            int PageCount=15;
            if(PageCount*(page-1)+PageCount<=answer.size()-1){
                PageAnswer=answer.subList(PageCount*(page-1),PageCount*(page-1)+PageCount);
            }else{
                PageAnswer=answer.subList(PageCount*(page-1),answer.size());
            }
        }

        //データの数を1ページ15件にする
        em.close();
        if(answer.size()>15){
            request.setAttribute("reports", PageAnswer);
        }else{
            request.setAttribute("reports", answer);
        }

        request.setAttribute("reports_count", answer.size());
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/follow/index.jsp");
        rd.forward(request, response);
    }

}