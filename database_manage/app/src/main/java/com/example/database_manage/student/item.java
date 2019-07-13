package com.example.database_manage.student;

public class item {
    String course_name;
    String course_time;
    String course_period;
    String course_weight;
    String teacher_name;
    Boolean ischeck;


    public String getCourse_weight() {
        return course_weight;
    }

    public void setCourse_weight(String course_weight)
    {
        this.course_weight = course_weight;
    }




    public  item(String course_name, String course_time, String course_period,String teacher_name,String course_weight)
    {
        this.course_name = course_name;
        this.course_time = course_time;
        this.course_period = course_period;
        this.teacher_name = teacher_name;
        this.course_weight = course_weight;
        this.ischeck = false;

    }
    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }
    public String getCourse_name() {
        return course_name;
    }

    public String getCourse_time() {
        return course_time;
    }

    public String getCourse_period() {
        return course_period;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public void setCourse_time(String course_time) {
        this.course_time = course_time;
    }

    public Boolean getIscheck() {
        return ischeck;
    }

    public void setCourse_period(String course_period) {
        this.course_period = course_period;
    }

    public void setIscheck(Boolean ischeck) {
        this.ischeck = ischeck;
    }
}
