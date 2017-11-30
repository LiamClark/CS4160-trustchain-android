package nl.tudelft.cs4160.trustchain_android.block

import nl.tudelft.cs4160.trustchain_android.block.TrustChainBlock.GENESIS_SEQ
import nl.tudelft.cs4160.trustchain_android.block.ValidationResult.ValidationStatus.*
import nl.tudelft.cs4160.trustchain_android.message.MessageProto
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TrustChainBlockValidationTest {
    val result = ValidationResult()

    //step2 tests
    @Test
    fun isolated_non_genesis_block() {
        val testBlock = test_block()
        TrustChainBlock.step2(testBlock, result, null, null)

        assertEquals(NO_INFO, result.status)
    }

    @Test
    fun isolated_genesis_block() {
        val testBlock = genesis_test_block()
        TrustChainBlock.step2(testBlock, result, null, null)

        assertEquals(PARTIAL_NEXT, result.status)
    }

    @Test
    fun no_prev_block_non_incrementing_seq_numbers_completely_partial() {
        val testBlock = test_block(2)
        TrustChainBlock.step2(testBlock, result, null, test_block(4))

        assertEquals(PARTIAL, result.status)
    }

    @Test
    fun no_prev_block_but_next_has_correct_seq_number() {
        val testBlock = test_block(2)
        TrustChainBlock.step2(testBlock, result, null, test_block(3))

        assertEquals(PARTIAL_PREVIOUS, result.status)
    }

    @Test
    fun genesis_block_has_incorrect_next_seq_number() {
        val testBlock = genesis_test_block()
        TrustChainBlock.step2(testBlock, result, null, test_block(3))

        assertEquals(PARTIAL_NEXT, result.status)
    }

    @Test
    fun no_next_block_correct_previous() {
        val testBlock = test_block(3)
        TrustChainBlock.step2(testBlock, result, test_block(2), null)

        assertEquals(PARTIAL_NEXT, result.status)
    }

    @Test
    fun no_next_block_incorrect_previous() {
        val testBlock = test_block(3)
        TrustChainBlock.step2(testBlock, result, test_block(1), null)

        assertEquals(PARTIAL, result.status)
    }

    @Test
    fun both_blocks_present_no_gaps() {
        val testBlock = test_block(3)
        TrustChainBlock.step2(testBlock, result, test_block(2), test_block(4))

        assertEquals(VALID, result.status)
    }

    @Test
    fun both_blocks_present_next_gap() {
        val testBlock = test_block(3)
        TrustChainBlock.step2(testBlock, result, test_block(2), test_block(5))

        assertEquals(PARTIAL_NEXT, result.status)
    }

    @Test
    fun both_blocks_present_prev_gap() {
        val testBlock = test_block(3)
        TrustChainBlock.step2(testBlock, result, test_block(1), test_block(4))

        assertEquals(PARTIAL_PREVIOUS, result.status)
    }

    @Test
    fun both_blocks_present_both_sides_gap() {
        val testBlock = test_block(3)
        TrustChainBlock.step2(testBlock, result, test_block(1), test_block(5))

        assertEquals(PARTIAL, result.status)
    }


    //step 3 tests
    val errorList: MutableList<String> = mutableListOf()

    @Test
    fun block_before_genesis() {
        val testBlock = test_block(0)
        TrustChainBlock.step3(testBlock, null, result, errorList)
        assertEquals(INVALID, result.status)
        assertEquals(1, errorList.size)
    }


    fun test_block() = test_block(0)

    fun test_block(seqNumber: Int) = MessageProto.TrustChainBlock.newBuilder().setSequenceNumber(seqNumber).build()

    fun genesis_test_block() = test_block(GENESIS_SEQ)
}