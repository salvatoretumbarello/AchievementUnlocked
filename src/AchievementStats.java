import java.util.Objects;

public class AchievementStats
{
    private Achievement achievement;
    private int n_unlocked; //Number of user that unlocked the achievement
    private int n_total;
    private double unlock_percentage;

    public AchievementStats(Achievement achievement)
    {
        this.achievement = achievement;
        this.n_total = 1;
        if (achievement.isUnlocked())
            this.n_unlocked = 1;
        else
            this.n_unlocked = 0;
    }

    public AchievementStats(Achievement achievement, int n_unlocked, int n_total, double unlock_percentage)
    {
        this.achievement = achievement;
        this.n_unlocked = n_unlocked;
        this.n_total = n_total;
        this.unlock_percentage = unlock_percentage;
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public String getDifficulty()
    {
        if (unlock_percentage >= 0.7)
            return "[VERY COMMON]";
        else if (unlock_percentage >= 0.5)
            return "[COMMON]";
        else if (unlock_percentage >= 0.3)
            return "[RARE]";
        else
            return "[VERY RARE]";
    }

    public int getN_unlocked ()
    {
        return n_unlocked;
    }

    public void setN_unlocked(int n_unlocked) {
        this.n_unlocked = n_unlocked;
    }

    public int getN_total() {
        return n_total;
    }

    public void setN_total(int n_total) {
        this.n_total = n_total;
    }

    public double getUnlock_percentage() {
        return unlock_percentage;
    }

    public void updateUnlockPercentage()
    {
        double a = n_unlocked, b = n_total;
        unlock_percentage = a / b;
    }

    public void increaseN_total()
    {
        n_total++;
    }
    public void increaseN_unlocked()
    {
        n_unlocked++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AchievementStats that = (AchievementStats) o;
        return Objects.equals(achievement, that.achievement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(achievement);
    }
}