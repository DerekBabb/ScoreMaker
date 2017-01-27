/**
 * @author Derek Babb <derek.babb@bpsne.net>
 * @version 1.0
 * @since 7-17-2013
 *
 * DigitReader is an interface that is used with ScoreMaker to test an 
 * optical character recognition algorithm for a scoreboard.
 *
 */

public interface DigitReader
{
  /**
   * setImage will accept a PictureEdit object.  The image should be saved within the class
   * so other methods may "read" the digit.
   *
   * @param digitPic is the picture of a single digit from a scoreboard.
   */
  public void setImage(PictureEdit digitPic);
 
  /**
   * getDigit will return the digit that the OCR algorithm beleives is the digit from the image
   * that has been previously loaded.
   *
   * @return a number 0-9 for the digit, return -1 if it cannot be read.
   */
  public int getDigit();
  
  /**
   * Each OCR should have a name for the purpose of listing in the menu.
   *
   * @return name of the OCR.
   */
  public String getName();
}