function table2string(cmd)
    local ret=""
    local i
    for i=1,#cmd do
        ret=ret..string.char(cmd[i])
    end
    return ret
end

function string2table(str)
    local tabret={}
    local i
    for i=1,#str do
        tabret[i]=str:byte(i)
    end
    return tabret
end

function string2hexstring(str)
	local ret = ''
	for i=1, #str do
		ret = ret .. string.format("%02x", str:byte(i))
	end
	return ret
end

function decode( cmd)
	local tb
	if cjson == nil then	
		cjson = (require 'JSON')
		tb = cjson:decode(cmd)
	else
		tb = cjson.decode(cmd)
	end
	return tb
end

function getKVTable(t,keyName,valueName)
	local kvTable = {}
	for i=1, #t do
		key = t[i][''..keyName]
		val = t[i][''..valueName]
		kvTable[''..key] = val
	end
	return kvTable
end

function table2json(t)
	local function serialize(tbl)
	    local tmp = {}
	    for k, v in pairs(tbl) do
		local k_type = type(k)
		local v_type = type(v)
		    local key = (k_type == "string" and "\"" .. k .. "\":") 
			or (k_type == "number" and "")
		    local value = (v_type == "table" and serialize(v))
			or (v_type == "boolean" and tostring(v))
			or (v_type == "string" and "\"" .. v .. "\"")
			or (v_type == "number" and v)
		    tmp[#tmp + 1] = key and value and tostring(key) .. tostring(value) or nil
	    end
	    if table.maxn(tbl) == 0 then
		    return "{" .. table.concat(tmp, ",") .. "}"
	    else
		    return "[" .. table.concat(tmp, ",") .. "]"
	    end
	end
	assert(type(t) == "table")
	return serialize(t)
end

function jds2pri( bizcode, cmd )
	--return err, length, bin
	local bin
	if bizcode == 1002 then
		local json = decode(cmd)
		local streams = json["streams"]
		local tabstreams = getKVTable(streams, 'stream_id', 'current_value')

		bin = {0xbb, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00,0xfa, 0x44}
		if (tabstreams["beep_switch"] == "0") then
			bin[2] = 3
		elseif (tabstreams["beep_switch"] == "1") then
			bin[2] = 2
		end

	elseif bizcode == 1004 then	--获取快照
		bin = {0xbb, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00,0xfa, 0x44}
	else				--错误的code指令,返回获取快照
		bin = {0xbb, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00,0xfa, 0x44}
	end

	local ret = table2string(bin)
	return 0, string.len(ret), ret
end

function pri2jds( bizcode, length, bin )
	--return err, jstr, bizcode
	if bizcode == 102 then
		return 0, '{"code":0,"streams":[{"current_value":1,"stream_id":"switch"}],"msg":"done"}', 102

	elseif bizcode == 104 then
		return 0, '{"code":0,"streams":[{"current_value":1,"stream_id":"switch"}],"msg":"done"}', 104

	elseif bizcode == 103 then
		return 0, '{"code":0,"streams":[{"current_value":1,"stream_id":"switch"}],"msg":"done"}', 103

	else
		return 0, '{"code":0,"streams":[{"current_value":1,"stream_id":"switch"}],"msg":"done"}', 102
	end
end


test = '{"code":0, "streams":[{"stream_id":"beep_switch","current_value":"0"},{"stream_id":"light","current_value":"on"}]}'
local code, len, ret = jds2pri(1002, test)
print("jds2pri(1002, test) Result:\r\n" .. string2hexstring(ret) )


local err, jstr, code = pri2jds(102, string.len(ret), ret)
print("\r\npri2jds(102, length, bin) Result:\r\n" .. jstr .. "\r\n")

