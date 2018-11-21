package za.co.knonchalant.candogram.domain;

/**
 * Created by evan on 2016/03/12.
 */
public class BaseDetail {
    private int step;

    public BaseDetail() {
    }

    public BaseDetail(int step) {
        this.step = step;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
}
