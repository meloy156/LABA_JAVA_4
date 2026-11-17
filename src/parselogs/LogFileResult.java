package parselogs;

import java.util.Objects;

public class LogFileResult {

    public final ArrayOrLinked collectionType;
    public int size;
    public long addAvg, removeAvg, addTotal, removeTotal;

    public LogFileResult(ArrayOrLinked collectionType, int size, long addAvg, long removeAvg, long addTotal, long removeTotal) {
        this.collectionType = collectionType;
        this.size = size;
        this.addAvg = addAvg;
        this.removeAvg = removeAvg;
        this.addTotal = addTotal;
        this.removeTotal = removeTotal;
    }

    public ArrayOrLinked getCollectionType() {
        return collectionType;
    }

    public int getSize() {
        return size;
    }

    public long getAddAvg() {
        return addAvg;
    }

    public long getRemoveAvg() {
        return removeAvg;
    }

    public long getAddTotal() {
        return addTotal;
    }


    public long getRemoveTotal() {
        return removeTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        LogFileResult that = (LogFileResult) o;
        return size == that.size && addAvg == that.addAvg && removeAvg == that.removeAvg && addTotal == that.addTotal && removeTotal == that.removeTotal && collectionType == that.collectionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(collectionType, size, addAvg, removeAvg, addTotal, removeTotal);
    }
    @Override
    public String toString() {
        return "logFileResult{" +
                "collectionType=" + collectionType +
                ", size=" + size +
                ", addAvg=" + addAvg +
                ", removeAvg=" + removeAvg +
                ", addTotal=" + addTotal +
                ", removeTotal=" + removeTotal +
                '}';
    }
}
