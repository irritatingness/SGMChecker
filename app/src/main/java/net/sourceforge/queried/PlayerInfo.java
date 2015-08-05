package net.sourceforge.queried;

/**
 * Player information that is returned by the player query.
 * 
 * @author DeadEd
 */
public class PlayerInfo {

  private String name;
  private String clan;
  private Integer kills;
  private Integer deaths;
  private Integer score;
  private Integer objectivesCompleted;
  private Integer ping;
  private Integer rate;

  public PlayerInfo() {
    name = "";
    clan = "";
  }

  /**
   * @return number of times this player has died, or {@code null} if this information was not provided by the server
   */
  public Integer getDeaths() {
    return deaths;
  }

  /**
   * @return the number of times this player has made a kill, or {@code null} if this information was not provided by
   *         the server
   */
  public Integer getKills() {
    return kills;
  }

  /**
   * @return the name of this player, or {@code null} if this information was not provided by the server
   */
  public String getName() {
    return name;
  }

  /**
   * @return the clan this player belongs to (Q4), or {@code null} if this information was not provided by the server
   */
  public String getClan() {
    return clan;
  }

  /**
   * @return the ping of this player, or {@code null} if this information was not provided by the server
   */
  public Integer getPing() {
    return ping;
  }

  /**
   * @return the rate settings of this player, or {@code null} if this information was not provided by the server
   */
  public Integer getRate() {
    return rate;
  }

  /**
   * @return the number of objectives completed by this player, or {@code null} if this information was not provided by
   *         the server
   */
  public Integer getObjectivesCompleted() {
    return objectivesCompleted;
  }

  /**
   * @return the score of this player, or {@code null} if this information was not provided by the server
   */
  public Integer getScore() {
    return score;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setClan(String clan) {
    this.clan = clan;
  }

  public void setKills(Integer kills) {
    this.kills = kills;
  }

  public void setDeaths(Integer deaths) {
    this.deaths = deaths;
  }

  public void setScore(Integer score) {
    this.score = score;
  }

  public void setObjectivesCompleted(Integer objectivesCompleted) {
    this.objectivesCompleted = objectivesCompleted;
  }

  public void setPing(Integer ping) {
    this.ping = ping;
  }

  public void setRate(Integer rate) {
    this.rate = rate;
  }

}
