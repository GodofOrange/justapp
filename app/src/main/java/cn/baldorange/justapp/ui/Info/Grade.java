package cn.baldorange.justapp.ui.Info;
/*
Grade grade = new Grade();
                                    grade.setId(it.next().text());
                                    grade.setStart(it.next().text());
                                    grade.setNumber(it.next().text());
                                    grade.setClaName(it.next().text());
                                    grade.setGrade(it.next().text());
                                    grade.setXuefen(it.next().text());
                                    grade.setAlltime(it.next().text());
                                    grade.setKaohe(it.next().text());
                                    grade.setShuxing(it.next().text());
                                    grade.setProp(it.next().text());
 */
public class Grade {
    private String id;
    private String start;
    private String number;
    private String claName;
    private String grade;
    private String xuefen;
    private String alltime;
    private String kaohe;
    private String shuxing;
    private String prop;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getClaName() {
        return claName;
    }

    public void setClaName(String claName) {
        this.claName = claName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getXuefen() {
        return xuefen;
    }

    public void setXuefen(String xuefen) {
        this.xuefen = xuefen;
    }

    public String getAlltime() {
        return alltime;
    }

    public void setAlltime(String alltime) {
        this.alltime = alltime;
    }

    public String getKaohe() {
        return kaohe;
    }

    public void setKaohe(String kaohe) {
        this.kaohe = kaohe;
    }

    public String getShuxing() {
        return shuxing;
    }

    public void setShuxing(String shuxing) {
        this.shuxing = shuxing;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }
}
