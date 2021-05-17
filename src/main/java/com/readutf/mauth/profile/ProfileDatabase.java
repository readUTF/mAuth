package com.readutf.mauth.profile;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public abstract class ProfileDatabase {

   @Getter
   List<Profile> profiles = new ArrayList<>();

    public abstract Profile getProfile(UUID uuid);
    public abstract void saveProfile(Profile profile);
}
