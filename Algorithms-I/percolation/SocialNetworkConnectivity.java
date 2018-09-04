/*
Social network connectivity. Given a social network containing n members and a log file containing m timestamps at which times pairs of members formed friendships, design an algorithm to determine the earliest time at which all members are connected (i.e., every member is a friend of a friend of a friend ... of a friend). Assume that the log file is sorted by timestamp and that friendship is an equivalence relation. The running time of your algorithm should be mlogn or better and use extra space proportional to n.
*/

package com.algorithms.socialnetworkconnectivity;

import com.algorithms.WeightedQuickUnionUF;
import com.util.StdOut;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SocialNetworkConnectivity {

    private final WeightedQuickUnionUF uf;
    private int numComponents;

    public SocialNetworkConnectivity(int N) {
        uf = new WeightedQuickUnionUF(N);
        numComponents = N;
    }

    public void addFriendship(int p1, int p2) {
        if (!uf.connected(p1, p2)) {
            --numComponents;
        }
        uf.union(p1,p2);
    }

    public boolean fullyConnected() {
        return this.numComponents == 1;
    }

    public static void main(String[] args) throws InterruptedException {
        
        // initialize social network data structure with N sites
        int n = 10;
        int m = 100;

        List<Relation> network = new ArrayList<Relation>(m);
        for (int i = 0; i < m; i++) {
            network.add(new Relation((int)(Math.random()*n), (int)(Math.random()*n), Calendar.getInstance().getTime()));
            Thread.sleep(100);
        }

        SocialNetworkConnectivity socialNetworkConnectivity = new SocialNetworkConnectivity(n);
        Date connectionDate = null;

        for (Relation r : network) {
            connectionDate = r.friendShipDate;
            socialNetworkConnectivity.addFriendship(r.friendOne, r.friendSecond);

            if(socialNetworkConnectivity.fullyConnected())
                break;
        }

        StdOut.println("Last member added at: " + network.get(m-1).friendShipDate);
        StdOut.println("All members were connected at: " + connectionDate);
    }
}