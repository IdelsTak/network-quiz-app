package company.policy.client.core;

import java.io.Serializable;

public class Attempt implements Serializable {

    private static final long serialVersionUID = 1L;
    private final int attempted;
    private final int correct;

    public Attempt(int attempted, int correct) {
        this.attempted = attempted;
        this.correct = correct;
    }

    public int getAttempted() {
        return attempted;
    }

    public int getCorrect() {
        return correct;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this.attempted;
        hash = 71 * hash + this.correct;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Attempt other = (Attempt) obj;
        if (this.attempted != other.attempted) {
            return false;
        }
        return this.correct == other.correct;
    }

    @Override
    public String toString() {
        return "Attempt{" + "attempted=" + attempted + ", correct=" + correct + '}';
    }

}
