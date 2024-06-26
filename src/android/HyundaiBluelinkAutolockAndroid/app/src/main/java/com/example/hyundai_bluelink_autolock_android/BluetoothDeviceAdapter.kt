import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.hyundai_bluelink_autolock_android.R

class BluetoothDeviceAdapter(
    private val devices: List<BluetoothDeviceInfo>,
    private val onClick: (BluetoothDeviceInfo) -> Unit
) : RecyclerView.Adapter<BluetoothDeviceAdapter.DeviceViewHolder>() {

    inner class DeviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val deviceName: TextView = itemView.findViewById(R.id.device_name)
        val deviceMac: TextView = itemView.findViewById(R.id.device_mac)

        fun bind(device: BluetoothDeviceInfo) {
            deviceName.text = device.name
            deviceMac.text = device.mac
            itemView.setOnClickListener { onClick(device) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bluetooth_device, parent, false)
        return DeviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(devices[position])
    }

    override fun getItemCount(): Int = devices.size
}
