package lv.fx.calculator.model.data

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
    val result: T?

    fun getData():T?
    fun setData(data: T)
}

interface IterableAPIResponse<T> : ApiResponseContext{
    val result: List<T>?

    fun getDataAsList(): List<T>
    fun setData(data: List<T>)
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
    override var ok: Boolean,
): BaseResponse(), BooleanAPIResponse{
    override var result: Boolean = false

    override fun setData(data: Boolean){
        result = data
    }
    override fun getData(): Boolean{
        return result
    }
}

data class SingleResponse<T>(
    override var ok: Boolean,
) : BaseResponse(), SingleAPIResponse<T>{
    override var result: T? = null

    override fun setData(data: T) {
        ok = true
        result = data
    }
    override fun getData(): T? {
        return result
    }
}

data class ListResponse<T>(
    override var ok: Boolean,
) : BaseResponse(), IterableAPIResponse<T>{
    override var result: List<T>? = null

    override fun setData(result: List<T>) {
        ok = true
        this.result = result
    }
    override fun getDataAsList(): List<T> {
        return result as List<T>
    }
}

