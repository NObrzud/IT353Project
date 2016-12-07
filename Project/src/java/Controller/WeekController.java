package Controller;

import DAO.WeekDAO;
import java.util.ArrayList;
import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(eager = false)
@SessionScoped
public class WeekController {

    private Date startDate;
    private Date endDate;

    public WeekController() {
        WeekDAO wdao = new WeekDAO();
        ArrayList<java.util.Date> dates = wdao.getWeek();
        startDate = dates.get(0);
        endDate = dates.get(1);
    }

    public void setNewWeek() {
        
        WeekDAO wdao = new WeekDAO();
        wdao.setNewWeek(new java.sql.Date(startDate.getTime()), new java.sql.Date(endDate.getTime()));
    }
    
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

}
