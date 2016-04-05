package com.sonbook.song.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Chant.
 */

@Document(collection = "chant")
public class Chant implements Serializable {

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("number")
    private Integer number;

    @Field("lyric")
    private String lyric;

    @Field("image")
    private Integer image;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Chant chant = (Chant) o;

        if ( ! Objects.equals(id, chant.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Chant{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", number='" + number + "'" +
            ", lyric='" + lyric + "'" +
            ", image='" + image + "'" +
            '}';
    }
}
