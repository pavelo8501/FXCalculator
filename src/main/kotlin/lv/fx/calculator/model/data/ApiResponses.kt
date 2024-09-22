package lv.fx.calculator.model.data

import lv.fx.calculator.model.data.SingleResponse

interface ApiResponseContext{
    var ok: Boolean
    var error: String?
    var errorCode: Int?

    fun setError(errorCode : Int, error: String){
        this.ok = false
        this.error = error
        this.errorCode = errorCode
    }
}

interface SingleAPIResponse<T> : ApiResponseContext{
    val result: T

    fun getData():T
    fun setData(data: T? = null)
}

interface IterableAPIResponse<T> : ApiResponseContext{
    val result: List<T>

    fun getDataAsList(): List<T>
    fun setData(data: List<T>? = null)
}

interface BooleanAPIResponse: ApiResponseContext{
    val result: Boolean

    fun getData(): Boolean
    fun setData(data: Boolean)
}

abstract class BaseResponse():ApiResponseContext
{
    override var ok: Boolean = false
    override var error: String? = null
    override var errorCode: Int? = null
}

data class BooleanResponse(
    override var result:Boolean,
): BaseResponse(), BooleanAPIResponse{

    init {
        if(result){
            ok = true
        }
    }

    override fun setData(data: Boolean){
        result = data
        if(data){
            ok = true
        }
    }
    override fun getData(): Boolean{
        return result
    }
}

data class SingleResponse<T>(
    override var result: T
) : BaseResponse(), SingleAPIResponse<T>{

    init {
        ok = true
    }
    constructor() : this(result = null as T) {
        ok = false
    }
    override fun setData(data: T?) {
        if(data != null){
            ok = true
            result = data
        }
    }
    override fun getData(): T {
        return result
    }
}

data class ListResponse<T>(
    override var result: List<T>
) : BaseResponse(), IterableAPIResponse<T>{


    init {
        ok = true
    }
    constructor() : this(result = emptyList()) {
        ok = false
    }

    override fun setData(result: List<T>?) {
        if(result != null){
            this.result = result
            ok = true
        }
    }
    override fun getDataAsList(): List<T> {
        return result
    }
}

