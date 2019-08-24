package ru.shaplov.models;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author shaplov
 * @since 24.08.2019
 */
@Entity
@Table(name = "picture_lob")
public class PictureLob implements IEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private byte[] img;

    @Column(name = "mime_type")
    private String mimeType;

    public PictureLob() {
    }

    public PictureLob(int id) {
        this.id = id;
    }

    @Override
    public int getId() {
        return id;
    }

    public byte[] getImg() {
        return img;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImg(byte[] img) {
        this.img = img;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PictureLob that = (PictureLob) o;
        return id == that.id
                && Arrays.equals(img, that.img)
                && Objects.equals(mimeType, that.mimeType);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, mimeType);
        result = 31 * result + Arrays.hashCode(img);
        return result;
    }
}
