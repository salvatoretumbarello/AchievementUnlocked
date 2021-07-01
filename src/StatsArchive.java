import java.util.HashSet;

public class StatsArchive
{
    private HashSet<AchievementStats> archive = new HashSet<>();

    public HashSet<AchievementStats> getArchive() {
        return archive;
    }

    public void updateStats(AchievementStats achievement)
    {
        for (AchievementStats as : archive)
        {
            if(as.equals(achievement))
            {
                AchievementStats as_tmp =
                        new AchievementStats(achievement.getAchievement());
                as_tmp.setN_total(as.getN_total() + 1);

                if (achievement.getAchievement().isUnlocked())
                    as_tmp.setN_unlocked(as.getN_unlocked() + 1);
                else
                    as_tmp.setN_unlocked(as.getN_unlocked());

                as_tmp.updateUnlockPercentage();
                archive.remove(achievement);
                archive.add(as_tmp);
                return;
            }
        }
        achievement.updateUnlockPercentage();
        archive.add(achievement);
    }

    public void add(AchievementStats achievement)
    {
        archive.add(achievement);
    }

}