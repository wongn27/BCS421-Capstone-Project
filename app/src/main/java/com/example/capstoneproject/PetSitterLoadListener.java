package com.example.capstoneproject;

import java.util.List;

public interface PetSitterLoadListener {
    void onAllPetSitterLoadSuccess(List<PetSitter> petSitterList);
    void onAllPetSitterLoadFailed(String message);
}
