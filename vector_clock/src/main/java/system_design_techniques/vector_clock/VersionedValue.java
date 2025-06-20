package system_design_techniques.vector_clock;

public class VersionedValue {
    public String value;
    public VectorClock clock;

    public VersionedValue(String value, VectorClock clock) {
        this.value = value;
        this.clock = clock.copy();
    }

    public String toString() {
        return value + " | " + clock;
    }
}
