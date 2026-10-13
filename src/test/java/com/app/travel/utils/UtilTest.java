package com.app.travel.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Base64;

import org.junit.jupiter.api.Test;

class UtilTest {

    @Test
    void generatedTokenDecodesToSixtyFourBytes() {
        byte[] tokenBytes = Base64.getDecoder().decode(Util.generateToken());

        assertEquals(64, tokenBytes.length);
    }
}