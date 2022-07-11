package com.flab.football.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Match 엔티티 클래스.
 */

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "`match`")
public class Match {

  @Getter
  public enum Rule {
    FIVE_VS_FIVE_TWO_TEAM, FIVE_VS_FIVE_THREE_TEAM,
    SIX_VS_SIX_TWO_TEAM, SIX_VS_SIX_THREE_TEAM
  }

  /**
   * 착용 가능 신발 제한 enum 클래스.
   */

  @Getter
  public enum LimitShoes {
    ALL, ONLY_FUTSAL_SHOES, ONLY_FOOTBALL_SHOES
  }

  /**
   * 참가자 레벨 제한 enum 클래스.
   */

  @Getter
  public enum LimitLevel {
    ALL, UNDER_BEGINNER, UNDER_AMATEUR, UPPER_PRO
  }

  /**
   * 성별 제한 enum 클래스.
   */

  @Getter
  public enum LimitGender {
    ALL, ONLY_MALE, ONLY_FEMALE
  }

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private int id;

  @Column(name = "start_time")
  private LocalDateTime startTime;

  @Column(name = "finish_time")
  private LocalDateTime finishTime;

  @Column(name = "min_number")
  private int min;

  @Column(name = "max_number")
  private int max;

  @Column(name = "rule")
  private Rule rule;

  @Column(name = "limit_shoes")
  private LimitShoes shoes;

  @Column(name = "limit_level")
  private LimitLevel level;

  @Column(name = "limit_gender")
  private LimitGender gender;

}
