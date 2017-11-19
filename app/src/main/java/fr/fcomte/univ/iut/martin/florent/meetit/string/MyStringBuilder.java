package fr.fcomte.univ.iut.martin.florent.meetit.string;

public final class MyStringBuilder {

    private final StringBuilder stringBuilder = new StringBuilder();

    public MyStringBuilder append(final String s) {
        stringBuilder.append(s);
        return this;
    }

    @Override
    public String toString() {
        stringBuilder.setLength(0);
        return stringBuilder.toString();
    }
}
