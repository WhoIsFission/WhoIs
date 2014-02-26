SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `WhoIsDev` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci ;
USE `WhoIsDev` ;

-- -----------------------------------------------------
-- Table `WhoIsDev`.`IPWHOIS`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `WhoIsDev`.`IPWHOIS` (
  `ip_start_address` MEDIUMTEXT NOT NULL,
  `ip_end_address` MEDIUMTEXT NOT NULL,
  `origin_as` MEDIUMTEXT NULL,
  `net_name` MEDIUMTEXT NULL,
  `net_handle` MEDIUMTEXT NULL,
  `parent` MEDIUMTEXT NULL,
  `net_type` MEDIUMTEXT NULL,
  `data_source` MEDIUMTEXT NULL,
  `description` MEDIUMTEXT NULL,
  `reg_date` TIMESTAMP NULL,
  `updated_date` TIMESTAMP NULL,
  `org_name` MEDIUMTEXT NULL,
  `org_id` MEDIUMTEXT NULL,
  `org_fax` MEDIUMTEXT NULL,
  `city` MEDIUMTEXT NULL,
  `state` MEDIUMTEXT NULL,
  `country` MEDIUMTEXT NULL,
  `postal_code` MEDIUMTEXT NULL,
  `org_reg_date` TIMESTAMP NULL,
  `org_updated_date` TIMESTAMP NULL,
  `is_current_data` TINYINT(1) NULL DEFAULT Y,
  `org_ref` MEDIUMTEXT NULL,
  `last_updated_time` TIMESTAMP NULL,
  PRIMARY KEY (`ip_start_address`, `ip_end_address`))
ENGINE = InnoDB;

CREATE INDEX `IPWHOIS_PK` ON `WhoIsDev`.`IPWHOIS` (`ip_start_address` ASC, `ip_end_address` ASC);


-- -----------------------------------------------------
-- Table `WhoIsDev`.`TECHCONTACT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `WhoIsDev`.`TECHCONTACT` (
  `ip_start_address` MEDIUMTEXT NOT NULL,
  `ip_end_address` MEDIUMTEXT NOT NULL,
  `tech_handle` MEDIUMTEXT NULL,
  `tech_name` MEDIUMTEXT NULL,
  `tech_phone` MEDIUMTEXT NULL,
  `tech_email` MEDIUMTEXT NULL,
  `tech_fax` MEDIUMTEXT NULL,
  `tech_ref` MEDIUMTEXT NULL,
  CONSTRAINT `TECHCONTACT_FK`
    FOREIGN KEY (`ip_start_address` , `ip_end_address`)
    REFERENCES `WhoIsDev`.`IPWHOIS` (`ip_start_address` , `ip_end_address`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `TECHCONTACT_FK_idx` ON `WhoIsDev`.`TECHCONTACT` (`ip_start_address` ASC, `ip_end_address` ASC);


-- -----------------------------------------------------
-- Table `WhoIsDev`.`ABUSECONTACT`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `WhoIsDev`.`ABUSECONTACT` (
  `ip_start_address` MEDIUMTEXT NOT NULL,
  `ip_end_address` MEDIUMTEXT NOT NULL,
  `abuse_handle` MEDIUMTEXT NULL,
  `abuse_name` MEDIUMTEXT NULL,
  `abuse_phone` MEDIUMTEXT NULL,
  `abuse_email` MEDIUMTEXT NULL,
  `abuse_ref` MEDIUMTEXT NULL,
  CONSTRAINT `ABUSECONTACT_FK`
    FOREIGN KEY (`ip_start_address` , `ip_end_address`)
    REFERENCES `WhoIsDev`.`IPWHOIS` (`ip_start_address` , `ip_end_address`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE INDEX `ABUSECONTACT_FK_idx` ON `WhoIsDev`.`ABUSECONTACT` (`ip_start_address` ASC, `ip_end_address` ASC);


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `WhoIsDev`.`IPWHOIS`
-- -----------------------------------------------------
START TRANSACTION;
USE `WhoIsDev`;
INSERT INTO `WhoIsDev`.`IPWHOIS` (`ip_start_address`, `ip_end_address`, `origin_as`, `net_name`, `net_handle`, `parent`, `net_type`, `data_source`, `description`, `reg_date`, `updated_date`, `org_name`, `org_id`, `org_fax`, `city`, `state`, `country`, `postal_code`, `org_reg_date`, `org_updated_date`, `is_current_data`, `org_ref`, `last_updated_time`) VALUES ('192.149.252.0', '192.149.252.255', 'AS10745', 'ARIN-NET', 'NET-192-149-252-0-1', 'NET-192-0-0-0-0', 'Direct Assignment', 'ARIN', NULL, '2012-09-07', '2012-09-19', 'ARIN Operations', 'ARINOPS', NULL, 'Chantilly', 'VA', 'US', '20151', '2012-09-07', '2012-09-19', Y, 'http://whois.arin.net/rest/org/ARINOPS', NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `WhoIsDev`.`TECHCONTACT`
-- -----------------------------------------------------
START TRANSACTION;
USE `WhoIsDev`;
INSERT INTO `WhoIsDev`.`TECHCONTACT` (`ip_start_address`, `ip_end_address`, `tech_handle`, `tech_name`, `tech_phone`, `tech_email`, `tech_fax`, `tech_ref`) VALUES ('192.149.252.0', '192.149.252.255', 'CHRIS167-ARIN', 'Christensen,Tim', '+1-703-227-0660', 'timc@arin.net', NULL, 'http://whois.arin.net/rest/poc/CHRIS167-ARIN');

COMMIT;


-- -----------------------------------------------------
-- Data for table `WhoIsDev`.`ABUSECONTACT`
-- -----------------------------------------------------
START TRANSACTION;
USE `WhoIsDev`;
INSERT INTO `WhoIsDev`.`ABUSECONTACT` (`ip_start_address`, `ip_end_address`, `abuse_handle`, `abuse_name`, `abuse_phone`, `abuse_email`, `abuse_ref`) VALUES ('192.149.252.0', '192.149.252.255', 'AOA4-ARIN', 'ARIN Operations Abuse', '+1-703-227-0660', 'abuse@arin.net', 'http://whois.arin.net/rest/poc/AOA4-ARIN');

COMMIT;

