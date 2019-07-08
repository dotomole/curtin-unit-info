//dotomole - 8/7/19
import java.io.Serializable;

public class Unit implements Serializable
{
    private static final long serialVersionUID = 1L;

    //private classfields
    private String unitCode;
    private String title;
    private String area;
    private String credits;
    private String contactHrs;
    private String prereqs;
    private String syllabus;
    private String unitLeads;

    public Unit()
    {

    }

    public Unit(String unitCode, String title, String area, String credits,
        String contactHrs, String syllabus, String prereqs, String unitLeads)
    {
        this.unitCode = unitCode;
        this.title = title;
        this.area = area;
        this.credits = credits;
        this.contactHrs = contactHrs;
        this.syllabus = syllabus;
        this.prereqs = prereqs;
        this.unitLeads = unitLeads;
    }

    //GETTERS
    public String getUnitCode()
    {
        return unitCode;
    }
    public String getTitle()
    {
        return title;
    }
    public String getArea()
    {
        return area;
    }
    public String getCredits()
    {
        return credits;
    }
    public String getContactHrs()
    {
        return contactHrs;
    }
    public String getSyllabus()
    {
        return syllabus;
    }
    public String getPrereqs()
    {
        return prereqs;
    }
    public String getUnitLeads()
    {
        return unitLeads;
    }

    @Override
    public String toString()
    {
        return ("\nUnit Code: "+unitCode+"\nTitle: "+title+"\nArea: "+area
            +"\nCredits: "+credits+"\nContact Hours: "+contactHrs+"\nPre-Requisites: "
            +prereqs+"\n\nSyllabus: "+syllabus+"\n\nUnit is a pre-requisite for:\n"+unitLeads);
    }
}