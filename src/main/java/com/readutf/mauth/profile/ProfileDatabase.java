package com.readutf.mauth.profile;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public abstract class ProfileDatabase {

   @Getter
   HashSet<Profile> profiles = new HashSet<>();

    public abstract Profile getProfile(UUID uuid);
    public abstract void saveProfile(Profile profile);
}
