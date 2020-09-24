package model;

public interface Visitable{
    void accept(Visitor visitor);
}