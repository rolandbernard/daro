package daro.lang.cli;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CliTest {

   @Test
   void lineNumber() {
      assertEquals(3, Cli.lineFromOffset(10, "0123\n567\n90123\n567"));
   }

   @Test
   void columnNumber() {
      assertEquals(2, Cli.columnFromOffset(10, "0123\n567\n90123\n567"));
   }

   @Test
   void lineNumberInEmptyText() {
      assertEquals(1, Cli.lineFromOffset(10, ""));
   }

   @Test
   void columnNumberInEmptyText() {
      assertEquals(11, Cli.columnFromOffset(10, ""));
   }

   @Test
   void lineNumberAtEnd() {
      assertEquals(3, Cli.lineFromOffset(14, "0123\n567\n90123\n"));
   }

   @Test
   void columnNumberAtEnd() {
      assertEquals(6, Cli.columnFromOffset(14, "0123\n567\n90123\n"));
   }
}
