package com.example.danhba;

public class DanhBa {
    private String name;
    private int id;
    private byte[] avatar;

    public DanhBa(String name, int id, byte[] avatar) {
        this.name = name;
        this.id = id;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}
