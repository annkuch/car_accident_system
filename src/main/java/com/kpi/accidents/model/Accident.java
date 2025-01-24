package com.kpi.accidents.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity(name = "Accident")
@Table(name = "accident")
@EqualsAndHashCode
@ToString

public class Accident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private int id;

    @Pattern(regexp = "((([A-Z])([a-z])+(\\s))(([A-Z])([a-z])+))|((([А-Я])([а-я])+(\\s))(([А-Я])([а-я])+))",
            message = "Поле повинно містити прізвище та ім'я з великої літери!")
    @Column(name = "name")
    private String name;

    @Size(min = 20, message = "Текст події повинен містити не менше 20 символів!")
    @Column(name = "text")
    private String text;

    @NotEmpty(message = "Введіть адресу події!")
    @Column(name = "address")
    private String address;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_accident", nullable = false)
    private LocalDate dateAccident;

    @Column(name = "time_accident", nullable = false)
    private LocalTime timeAccident;

    // ManyToOne relationship with AccidentType
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accident_type_id", nullable = false)
    private AccidentType accidentType;

    // ManyToOne relationship with Rule
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accident_rule_id", nullable = false)
    private Rule accidentRule;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getDateAccident() {
        return dateAccident;
    }

    public void setDateAccident(LocalDate dateAccident) {
        this.dateAccident = dateAccident;
    }

    public LocalTime getTimeAccident() {
        return timeAccident;
    }

    public void setTimeAccident(LocalTime timeAccident) {
        this.timeAccident = timeAccident;
    }

    public AccidentType getAccidentType() {
        return accidentType;
    }

    public void setAccidentType(AccidentType accidentType) {
        this.accidentType = accidentType;
    }

    public Rule getAccidentRule() {
        return accidentRule;
    }

    public void setAccidentRule(Rule accidentRule) {
        this.accidentRule = accidentRule;
    }
}
