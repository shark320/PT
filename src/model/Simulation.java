package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Simulation {

    private final List<Warehouse> warehouses = new ArrayList<>();

    private final List<Oasis> oases = new ArrayList<>();

    private final List<Camel> camels = new LinkedList<>();

    private final List<Request> requests = new LinkedList<>();

    /**
     * Road map warehouses <--> oases and oases <--> oases
     */
    private final boolean[][] roads;

    private int roadsCount;

    public Simulation(String[] params) {
        int i = 0;
        int warehouseCount = Integer.parseInt(params[i++]);
        for (int j = 0; j < warehouseCount; ++j) {
            Warehouse w = new Warehouse(
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++])
            );
            warehouses.add(w);
        }
        int oasesCount = Integer.parseInt(params[i++]);
        for (int j = 0; j < oasesCount; ++j) {
            Oasis o = new Oasis(
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++])
            );
            oases.add(o);
        }
        roads = new boolean[warehouseCount+oasesCount][oasesCount+warehouseCount];
        this.roadsCount = Integer.parseInt(params[i++]);
        for (int j = 0; j < this.roadsCount; ++j) {
            roads[Integer.parseInt(params[i++])-1][Integer.parseInt(params[i++])-1] = true;
        }

        int camelsCount = Integer.parseInt(params[i++]);

        for (int j = 0; j < camelsCount; ++j) {
            Camel c = new Camel(
                    params[i++],
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Double.parseDouble(params[i++])
            );
            camels.add(c);
        }

        int requestsCount = Integer.parseInt(params[i++]);
        for (int j = 0; j < requestsCount; j++) {
            Request r = new Request(
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++]),
                    Integer.parseInt(params[i++])
            );
            requests.add(r);
        }
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append("-------------------------------->>WAREHOUSES [").append(warehouses.size()).append("]<<--------------------------------\n\n");
        for (Warehouse warehouse : warehouses) {
            str.append(warehouse.toString()).append("\n");
        }

        str.append("\n-------------------------------->>OASES [").append(oases.size()).append("]<<--------------------------------\n\n");
        for (Oasis oasis : oases) {
            str.append(oasis.toString()).append("\n");
        }

        str.append("\n-------------------------------->>ROADS [").append(this.roadsCount).append("]<<--------------------------------\n\n");
        int road = 0;
        for (int i = 0; i < roads.length; ++i) {
            for (int j = 0; j < roads[i].length; ++j) {
                if (roads[i][j]) {
                    str.append("[").append(++road).append("] ").append(i+1).append("<-->").append(j+1).append("\n");
                }
            }
        }

        str.append("\n-------------------------------->>CAMELS [").append(camels.size()).append("]<<--------------------------------\n\n");
        for(Camel camel: camels){
            str.append(camel.toString()).append("\n");
        }

        str.append("\n-------------------------------->>REQUESTS [").append(requests.size()).append("]<<--------------------------------\n\n");
        for(Request request : requests){
            str.append(request.toString()).append("\n");
        }

        return str.toString();
    }
}
