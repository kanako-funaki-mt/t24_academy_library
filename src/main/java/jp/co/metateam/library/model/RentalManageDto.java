package jp.co.metateam.library.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import jp.co.metateam.library.values.RentalStatus;
import java.util.Optional;

/**
 * 貸出管理DTO
 */
@Getter
@Setter
public class RentalManageDto {

    private Long id;

    @NotEmpty(message="在庫管理番号は必須です")
    private String stockId;

    @NotEmpty(message="社員番号は必須です")
    private String employeeId;

    @NotNull(message="貸出ステータスは必須です")
    private Integer status;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message="貸出予定日は必須です")
    private Date expectedRentalOn;

    @DateTimeFormat(pattern="yyyy-MM-dd")
    @NotNull(message="返却予定日は必須です")
    private Date expectedReturnOn;

    private Timestamp rentaledAt;

    private Timestamp returnedAt;

    private Timestamp canceledAt;

    private Stock stock;

    private Account account;

    public Optional<String> isStatusError(Integer oldStatus, Integer status) {
        if (oldStatus == RentalStatus.RENT_WAIT.getValue()&& status == RentalStatus.RETURNED.getValue()) {
            return Optional.of("貸出待ちから返却済みは選択できません");
        } else if (oldStatus == RentalStatus.RENTAlING.getValue()&& status == RentalStatus.RENT_WAIT.getValue()) {
            return Optional.of("貸出中から貸出待ちは選択できません");
        }else if(oldStatus == RentalStatus.RENTAlING.getValue()&& status == RentalStatus.CANCELED.getValue()) {
            return Optional.of("貸出中からキャンセルは選択できません");
        }else if(oldStatus == RentalStatus.RETURNED.getValue()&& status == RentalStatus.RENT_WAIT.getValue()) {
            return Optional.of("貸出済みから貸出待ちは選択できません");
        }else if(oldStatus == RentalStatus.RETURNED.getValue()&& status == RentalStatus.RENTAlING.getValue()) {
            return Optional.of("貸出済みから貸出中は選択できません");
        }else if(oldStatus == RentalStatus.RETURNED.getValue()&& status == RentalStatus.CANCELED.getValue()) {
            return Optional.of("貸出済みからキャンセルは選択できません");
        }else if(oldStatus == RentalStatus.CANCELED.getValue()&& status == RentalStatus.RENT_WAIT.getValue()) {
            return Optional.of("キャンセルから貸出待ちは選択できません");
        }else if(oldStatus == RentalStatus.CANCELED.getValue()&& status == RentalStatus.RENTAlING.getValue()) {
            return Optional.of("キャンセルから貸出中は選択できません");
        }else if(oldStatus == RentalStatus.CANCELED.getValue()&& status == RentalStatus.RETURNED.getValue()) {
            return Optional.of("キャンセルから貸出済みは選択できません");
        }else{
            return Optional.empty(); // 遷移が正しい場合は空のOptionalを返す
        }
    }

    



    public void dateCheck() throws Exception {
        LocalDate expectedRentalOnLocalDate = this.expectedRentalOn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate expectedReturnOnLocalDate = this.expectedReturnOn.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if (expectedRentalOnLocalDate.isAfter(expectedReturnOnLocalDate)) {
            throw new Exception("返却予定日は貸出予定日よりも後に設定してください");
        }
    }
}
