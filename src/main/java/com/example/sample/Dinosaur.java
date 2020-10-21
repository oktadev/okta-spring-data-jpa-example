package com.example.sample;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Getter @Setter @NoArgsConstructor
public class Dinosaur {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    private String name;

    private boolean fangs;

    private int numberOfArms;

    private double weightTons;

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%d, name='%s', fangs='%b', numberOfArms='%d', weightTons='%f']",
                id, name, fangs, numberOfArms, weightTons);
    }

}
