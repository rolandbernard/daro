package daro.lang.interpreter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import daro.lang.values.*;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

public class IntegerInterpreterTest {
    private Interpreter interpreter;

    @BeforeEach
    void initializeInterpreter() {
        interpreter = new Interpreter();
    }

    @Test
    void singleInteger() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("42"));
    }

    @Test
    void defaultInteger() {
        assertEquals(new UserInteger(BigInteger.valueOf(0)), interpreter.execute("new int"));
    }

    @Test
    void integerFromReal() {
        assertEquals(new UserInteger(BigInteger.valueOf(5)), interpreter.execute("new int { 5.5 }"));
    }

    @Test
    void addition() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("33 + 9"));
    }

    @Test
    void subtraction() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("51 - 9"));
    }

    @Test
    void multiplication() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("3 * 14"));
    }

    @Test
    void division() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("3842 / 91"));
    }

    @Test
    void divisionByZero() {
        assertThrows(InterpreterException.class, () -> {
            interpreter.execute("3842 / 0");
        });
    }

    @Test
    void remainder() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("3864 % 91"));
    }

    @Test
    void shiftLeft() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("21 << 1"));
    }

    @Test
    void shiftRight() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("2688 >> 6"));
    }

    @Test
    void bitwiseAnd() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("0b111101010 & 0b111111"));
    }

    @Test
    void bitwiseOr() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("0b100000 | 0b001010"));
    }

    @Test
    void bitwiseXor() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("0b100100 ^ 0b001110"));
    }

    @Test
    void bitwiseNot() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("~(-43)"));
    }

    @Test
    void negative() {
        assertEquals(new UserInteger(BigInteger.valueOf(-42)), interpreter.execute("-42"));
    }

    @Test
    void positive() {
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("+42"));
    }

    @Test
    void equals() {
        assertEquals(new UserBoolean(true), interpreter.execute("42 == 42"));
        assertEquals(new UserBoolean(false), interpreter.execute("42 == 12"));
    }

    @Test
    void notEquals() {
        assertEquals(new UserBoolean(true), interpreter.execute("42 != 12"));
        assertEquals(new UserBoolean(false), interpreter.execute("42 != 42"));
    }

    @Test
    void lessThan() {
        assertEquals(new UserBoolean(true), interpreter.execute("12 < 42"));
        assertEquals(new UserBoolean(false), interpreter.execute("42 < 42"));
    }

    @Test
    void lessOrEqual() {
        assertEquals(new UserBoolean(true), interpreter.execute("12 <= 42"));
        assertEquals(new UserBoolean(true), interpreter.execute("42 <= 42"));
        assertEquals(new UserBoolean(false), interpreter.execute("42 <= 12"));
    }

    @Test
    void moreThan() {
        assertEquals(new UserBoolean(true), interpreter.execute("42 > 12"));
        assertEquals(new UserBoolean(false), interpreter.execute("42 > 42"));
    }

    @Test
    void moreOrEqual() {
        assertEquals(new UserBoolean(true), interpreter.execute("42 >= 12"));
        assertEquals(new UserBoolean(true), interpreter.execute("42 >= 42"));
        assertEquals(new UserBoolean(false), interpreter.execute("12 >= 42"));
    }

    @Test
    void integerType() {
        assertEquals(new UserTypeInteger(), interpreter.execute("typeof(42)"));
    }

    @Test
    void power() {
        assertEquals(new UserInteger(new BigInteger("215902086207539658325165336743120720122153151163171746970076083020174674294093250081322377403206290806602254624791716027404589502706882595560841685684627532849694555561189249986531159993684195562963700422608118025892311524377777262700392501421084646576666267244585581537020651726668786217736360304494411969100604968440670234608193104203900376861222891652004101869440971475574237709212659842715105515388564115763277850040418389904586902146223099038927823233679193682765336171872435908740058624430393269955793038739800002155041942268639394234359791412830801755610928329546136962941731200609130361707626534837520580117137662795426347126205816109922985936779374345773653569911183949088570460769800705554306779766101173735950059315139259233687772383655534108962124327019396261813346996682936450191747761068822268261178479544236364220606969201270328365700347914537680885087780765247560445757482632946488672456949253638476853725008484340294226042534340483906195836121531629991871921139826666923468988336932685476287312628096187533018927604249800783648733669427792605236222540937907340702726408008545938721599259591053306361723697206444597220213648482476143142260279118641846059341033310692208203132209488612495982363540058591088598056058636968447126053742199110090375308902545916697637362701363522181784994447945416677742605341710589643490065340235436035409633958368044557734349199828931362838331351762130620077009392420391428112789889474254635703617471869611845071538379680454729369716181372296929412042985601552216752537560927628573658926588347012908244668518670100690346725930390255424446976843394682507120344666214784563154711542470932348574115620917974530725681819915601712141974428754860486215145369251826358144408794702864271813509424788417702452120989189626976415428367688063789673865479500915623960042583383865468355069327158045599548695073567100192010201334792914606723523203345867141718217849170821784459277086229757698640989142984760710036945716923695995167905736813362637205623759375575886991858184624580103562287939608426232738474985617592873291965009788638861415271033487744592503931492037291599274520478932614729619392185976612277263304396415520609904339659976672102273673665833488635339457717682502028417098318392748356452681503538765155603160675504885196928201113703142384439910739083538310173376394730288296069025123847123957663417088335341901008311930986810883241560147319123613654087963407175628190751912048318742075711757325137515576399455259010813410059610420849856269450318935199045197105109960415977767245929858632106078629419012770084672787234259292398442236390194933376854668596029335751106933664646280109901546519132288307871898119242479883236030921291234689788184266128565804419144320788118735525497355948727389338569902786215021677332220647007634729528335745248832278195246858163138514144079943475651617349883486103261085795973492925002210657719450677396825732959989446855569293351993090205883021907792320344008108003537427560935277756771")), interpreter.execute("971**991"));
    }

    @Test
    void addAssign() {
        interpreter.execute("x = 32; x += 10");
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("x"));
    }

    @Test
    void subtractAssign() {
        interpreter.execute("x = 67; x -= 25");
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("x"));
    }

    @Test
    void multiplyAssign() {
        interpreter.execute("x = 14; x *= 3");
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("x"));
    }

    @Test
    void divideAssign() {
        interpreter.execute("x = 294; x /= 7");
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("x"));
    }

    @Test
    void remainderAssign() {
        interpreter.execute("x = 735; x %= 99");
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("x"));
    }

    @Test
    void shiftLeftAssign() {
        interpreter.execute("x = 21; x <<= 1");
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("x"));
    }

    @Test
    void shiftRightAssign() {
        interpreter.execute("x = 168; x >>= 2");
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("x"));
    }

    @Test
    void bitwiseAndAssign() {
        interpreter.execute("x = 0b111101010; x &= 0b111111");
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("x"));
    }

    @Test
    void bitwiseOrAssign() {
        interpreter.execute("x = 0b100000; x |= 0b001010");
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("x"));
    }

    @Test
    void bitwiseXorAssign() {
        interpreter.execute("x = 0b100100; x ^= 0b001110");
        assertEquals(new UserInteger(BigInteger.valueOf(42)), interpreter.execute("x"));
    }
}
