package nyu.edu.dto;

public class Args {

    public Args(double df, double tol, double iter, boolean isMin) {
        this.df = df;
        this.tol = tol;
        this.iter = iter;
        this.isMin = isMin;
    }

    private double df;
    private double tol;
    private double iter;
    private boolean isMin;

    public double getDf() {
        return df;
    }

    public void setDf(double df) {
        this.df = df;
    }

    public double getTol() {
        return tol;
    }

    public void setTol(double tol) {
        this.tol = tol;
    }

    public double getIter() {
        return iter;
    }

    public void setIter(double iter) {
        this.iter = iter;
    }

    public boolean isMin() {
        return isMin;
    }

    public void setMin(boolean min) {
        isMin = min;
    }
}
