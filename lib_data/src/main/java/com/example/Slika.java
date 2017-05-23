package com.example;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.swing.text.DateFormatter;

/**
 * Created by Tadej on 26.2.2017.
 */

public class Slika {
    private String idSlike;
    private String imeSlike;
    private Double sirinaZeml;
    private Double visinaZeml;
    private String datum;
    private String pathFile;
    private String idUser;
    private Date date;





    public Slika(String imeSlike, Double sirinaZeml, Double visinaZeml, String datum, String pathFile, String idUser) {
        this.imeSlike = imeSlike;
        this.sirinaZeml = sirinaZeml;
        this.visinaZeml = visinaZeml;
        this.datum = datum;
        this.pathFile = pathFile;
        this.idUser = idUser;
        this.idSlike =  UUID.randomUUID().toString().replaceAll("-", "");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            this.date = sdf.parse(datum);
        } catch (ParseException e) {
            e.printStackTrace();
    }
    }

    public double getSirinaZeml() {
        return sirinaZeml;
    }

    public String getIdSlike() {
        return idSlike;
    }

    public Date getDateComp() {
        return date;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public void setSirinaZeml(Double sirinaZeml) {
        this.sirinaZeml = sirinaZeml;
    }

    public double getVisinaZeml() {
        return visinaZeml;
    }

    public void setVisinaZeml(Double visinaZeml) {
        this.visinaZeml = visinaZeml;
    }

    public String getImeSlike() {
        return imeSlike;
    }

    public void setImeSlike(String imeSlike) {
        this.imeSlike = imeSlike;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getPathFile() {
        return pathFile;
    }

    public void setPathFile(String pathFile) {
        this.pathFile = pathFile;
    }

    @Override
    public String toString() {
        return "\n\n    Slika: {" +
                "imeSlike='" + imeSlike + '\'' +
                ",\n sirinaZeml=" + sirinaZeml +
                ",\n visinaZeml=" + visinaZeml +
                ",\n datum=" + datum +
                ",\n pathFile='" + pathFile + '\'' +
                '}';
    }
}
