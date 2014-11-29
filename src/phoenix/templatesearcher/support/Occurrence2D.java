package phoenix.templatesearcher.support;

import phoenix.templatesearcher.api.IOccurrence2D;

import java.util.Objects;

public class Occurrence2D implements IOccurrence2D {
    private final int x;
    private final int y;
    private final int templateID;

    public Occurrence2D(int x, int y, int templateID) {
        this.x = x;
        this.y = y;
        this.templateID = templateID;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getTemplateID() {
        return templateID;
    }

    @Override
    public String toString() {
        return String.format("(%d, %d) - %d", x, y, templateID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, templateID);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof IOccurrence2D)) {
            return false;
        }

        IOccurrence2D occ = (IOccurrence2D) obj;
        return getX() == occ.getX() && getY() == occ.getY() && templateID == occ.getTemplateID();
    }
}
