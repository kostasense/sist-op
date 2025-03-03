/* Alumna: Olivas Quiñonez Grecia
 * No. Control: 23170266
 */
public class Pair<T, N extends Comparable<N>> {
    T first;
    N second;

    public Pair(T first, N second) {
        this.first = first;
        this.second = second;
    }

    public int compareTo(Pair<T, N> o) {
        return second.compareTo(o.second);
    }

    public T first() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    public N second() {
        return second;
    }

    public void setSecond(N second) {
        this.second = second;
    }

    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}