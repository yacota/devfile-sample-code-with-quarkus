package org.acme;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {

    public static void main(String ... args) {
        System.out.println("Running sample app");
        Quarkus.run(args);
    }

}