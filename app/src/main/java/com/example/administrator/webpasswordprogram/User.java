package com.example.administrator.webpasswordprogram;
public class User{

    private String id;
    private String pw;
    private String session_key;
    private String name;
    private String grade;
    private String email;

    public User()
    {

        id = "id";

    }
    public User(String _id,String _pw,String _session_key,String _name,String _grade)
    {
        id = _id;
        pw = _pw;
        session_key = _session_key;
        name = _name;
        grade = _grade;
    }

    public void setid(String _id){
        id = _id;
    }

    public void setpw(String _pw){
        pw = _pw;
    }

    public void setsession_key(String _session_key){
        session_key = _session_key;
    }

    public void setname(String _name){
        name = _name;
    }

    public void setgrade(String _grade){
        grade = _grade;
    }

    public void setemail(String _email){
        email = _email;
    }

    public String getid(){
        return id;
    }

    public String getpw(){
        return pw;
    }

    public String getsession_key(){
        return session_key;
    }

    public String getname(){
        return name;
    }

    public String getgrade(){
        return grade;
    }

    public String getemail(){
        return email;
    }

}
