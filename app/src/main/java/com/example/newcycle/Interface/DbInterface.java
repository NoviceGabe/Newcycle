package com.example.newcycle.Interface;

import com.example.newcycle.Interface.OnReadClient;
import com.example.newcycle.Interface.OnWriteClient;

public interface DbInterface{
    void write(OnWriteClient client);
    void read(OnReadClient client);
}
